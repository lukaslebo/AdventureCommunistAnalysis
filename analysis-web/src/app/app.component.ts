import {Component, OnDestroy} from '@angular/core';
import {BEM} from './util/bem';
import {REM} from './util/rem';
import {select, Store} from '@ngrx/store';
import {selectCurrentRank, selectResearcher} from './reducer/analysis.reducer';
import {Observable, Subject} from 'rxjs';
import {Researcher, ResearcherStateMap, Section} from './reducer/analysis.state';
import {Researchers} from './reducer/researcher.state';
import {updateRank, updateResearcher, UpdateResearcherProps} from './reducer/analysis.actions';
import {FormControl} from '@angular/forms';
import {takeUntil} from 'rxjs/operators';

@Component({
	selector: 'analysis-app',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
	readonly blockName = 'analysis-app';
	readonly styles = BEM(this.blockName);
	readonly dimensions = REM();

	readonly Researchers = Researchers;
	researcherStateMap$: Observable<ResearcherStateMap>;
	rank$: Observable<number>;
	readonly rankControl = new FormControl(null);
	readonly destroyed$ = new Subject<void>();

	readonly sections = [
		'Potato',
		'Land',
		'Ore',
		'Military',
		'Placebo',
		'Supreme',
		'Trade'
	];
	readonly researcherBySection: Record<Section, Researcher[]>;

	constructor(private readonly store: Store) {
		this.researcherBySection = {
			Potato: Researchers.allResearchers.filter(
				(r) => r.industry === 'Potato' && r.modifier !== 'Trade'
			),
			Land: Researchers.allResearchers.filter(
				(r) => r.industry === 'Land' && r.modifier !== 'Trade'
			),
			Ore: Researchers.allResearchers.filter(
				(r) => r.industry === 'Ore' && r.modifier !== 'Trade'
			),
			Military: Researchers.allResearchers.filter(
				(r) => r.industry === 'Military' && r.modifier !== 'Trade'
			),
			Placebo: Researchers.allResearchers.filter(
				(r) => r.industry === 'Placebo' && r.modifier !== 'Trade'
			),
			Supreme: Researchers.allResearchers.filter(
				(r) => r.rarity === 'Supreme' && r.modifier !== 'Trade'
			),
			Trade: Researchers.allResearchers.filter((r) => r.modifier === 'Trade')
		};

		this.researcherStateMap$ = store.pipe(select(selectResearcher));
		this.rank$ = store.pipe(select(selectCurrentRank));

		this.rankControl.valueChanges
			.pipe(takeUntil(this.destroyed$))
			.subscribe((rank) => this.store.dispatch(updateRank({rank})));
		this.rank$
			.pipe(takeUntil(this.destroyed$))
			.subscribe((rank) =>
				this.rankControl.patchValue(rank, {emitEvent: false})
			);
	}

	updateResearcherState(props: UpdateResearcherProps) {
		this.store.dispatch(updateResearcher(props));
	}

	trackByResearcherId(index: number, el: Researcher) {
		return el.id;
	}

	rankUp() {
		const next = (this.rankControl.value ?? 0) + 1;
		this.rankControl.setValue(next);
	}

	rankDown() {
		const next = Math.max((this.rankControl.value ?? 0) - 1, 1);
		this.rankControl.setValue(next);
	}

	ngOnDestroy() {
		this.destroyed$.next();
	}
}
