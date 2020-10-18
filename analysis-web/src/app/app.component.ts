import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { BEM } from './util/bem';
import { REM } from './util/rem';
import { select, Store } from '@ngrx/store';
import {
	selectAnalysis,
	selectCurrentRank,
	selectResearcher
} from './reducer/analysis.reducer';
import { Observable, Subject } from 'rxjs';
import {
	AnalysisMap,
	Researcher,
	ResearcherStateMap,
	Section,
	sections
} from './reducer/analysis.state';
import { Researchers } from './reducer/researcher.state';
import {
	copyToClipboard,
	loadCookies,
	loadFromRoute,
	updateRank,
	updateResearcher,
	UpdateResearcherProps
} from './reducer/analysis.actions';
import { FormControl } from '@angular/forms';
import { debounceTime, take, takeUntil } from 'rxjs/operators';
import { Decimal } from 'decimal.js';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'analysis-app',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
	readonly blockName = 'analysis-app';
	readonly styles = BEM(this.blockName);
	readonly dimensions = REM();

	readonly researcherStateMap$: Observable<ResearcherStateMap>;
	readonly rank$: Observable<number>;
	readonly analysisMap$: Observable<AnalysisMap>;
	readonly rankControl = new FormControl(null, { updateOn: 'blur' });
	readonly sortControl = new FormControl(false);
	readonly destroyed$ = new Subject<void>();

	readonly sections = sections;
	readonly researcherBySection: Record<Section, Researcher[]>;

	constructor(
		private readonly store: Store,
		private readonly cdRef: ChangeDetectorRef,
		private readonly route: ActivatedRoute
	) {
		this.route.queryParams
			.pipe(debounceTime(10), take(1), takeUntil(this.destroyed$))
			.subscribe((params) => {
				if (params.r || params.s) {
					this.store.dispatch(
						loadFromRoute({
							rank: params.r,
							encodedState: params.s
						})
					);
				} else {
					this.store.dispatch(loadCookies());
				}
			});

		// prettier-ignore
		this.researcherBySection = {
			Potato: Researchers.allResearchers.filter((r) => r.industry === 'Potato' && r.modifier !== 'Trade'),
			Land: Researchers.allResearchers.filter((r) => r.industry === 'Land' && r.modifier !== 'Trade'),
			Ore: Researchers.allResearchers.filter((r) => r.industry === 'Ore' && r.modifier !== 'Trade'),
			Military: Researchers.allResearchers.filter((r) => r.industry === 'Military' && r.modifier !== 'Trade'),
			Placebo: Researchers.allResearchers.filter((r) => r.industry === 'Placebo' && r.modifier !== 'Trade'),
			Supreme: Researchers.allResearchers.filter((r) => r.rarity === 'Supreme' && r.modifier !== 'Trade'),
			Trade: Researchers.allResearchers.filter((r) => r.modifier === 'Trade')
		};

		this.researcherStateMap$ = store.pipe(select(selectResearcher));
		this.rank$ = store.pipe(select(selectCurrentRank));
		this.analysisMap$ = store.pipe(select(selectAnalysis));

		this.rankControl.valueChanges
			.pipe(takeUntil(this.destroyed$))
			.subscribe((rank) => this.store.dispatch(updateRank({ rank })));
		this.rank$
			.pipe(takeUntil(this.destroyed$))
			.subscribe((rank) =>
				this.rankControl.patchValue(rank, { emitEvent: false })
			);

		this.sortControl.valueChanges
			.pipe(takeUntil(this.destroyed$))
			.subscribe(() => this.cdRef.detectChanges());
	}

	updateResearcherState(props: UpdateResearcherProps) {
		this.store.dispatch(updateResearcher(props));
	}

	trackByResearcherId(index: number, el: Researcher) {
		return el.name;
	}

	rankUp() {
		const next = (this.rankControl.value ?? 0) + 1;
		this.rankControl.setValue(next);
	}

	rankDown() {
		const next = Math.max((this.rankControl.value ?? 0) - 1, 1);
		this.rankControl.setValue(next);
	}

	filterAndSort(
		researchers: Researcher[],
		analysisMap: AnalysisMap,
		rank: number
	) {
		const res = researchers.filter((r) => r.unlockedAtRank <= rank);
		if (!this.sortControl.value) return res;
		return res.sort((a, b) => {
			const rA = new Decimal(analysisMap[a.name].boostPer1kScience);
			const rB = new Decimal(analysisMap[b.name].boostPer1kScience);
			return rA.comparedTo(rB) * -1;
		});
	}

	copyToClipboard() {
		this.store.dispatch(copyToClipboard());
	}

	ngOnDestroy() {
		this.destroyed$.next();
	}
}
