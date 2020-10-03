import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {BEM} from '../../util/bem';
import {REM} from '../../util/rem';
import {Researcher, ResearcherState} from '../../reducer/analysis.state';
import {FormBuilder, FormControl} from '@angular/forms';
import {BehaviorSubject, Subject} from 'rxjs';
import {initialResearcherState} from '../../reducer/analysis.reducer';
import {takeUntil} from 'rxjs/operators';
import {UpdateResearcherProps} from '../../reducer/analysis.actions';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field';

@Component({
	selector: 'researcher-analysis-row',
	templateUrl: './researcher-analysis-row.component.html',
	styleUrls: ['./researcher-analysis-row.component.scss'],
	providers: [
		{
			provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
			useValue: {appearance: 'outline', floatLabel: 'auto'}
		}
	]
})
export class ResearcherAnalysisRowComponent implements OnDestroy {
	readonly blockName = 'researcher-analysis-row';
	readonly styles = BEM(this.blockName);
	readonly dimensions = REM();

	@Input()
	researcher: Researcher;

	@Input()
	set researcherState(researcherState: ResearcherState) {
		this.researcherState$.next(researcherState);
	}

	get researcherState() {
		return this.researcherState$.getValue();
	}

	researcherState$ = new BehaviorSubject<ResearcherState>(
		initialResearcherState
	);

	@Output()
	updated = new EventEmitter<UpdateResearcherProps>();

	formControls: Record<keyof ResearcherState, FormControl> = {
		currentLevel: new FormControl(null),
		availableCards: new FormControl(null)
	};
	formGroup = this.formBuilder.group(this.formControls, {updateOn: 'blur'});

	destroyed$ = new Subject<void>();

	constructor(private readonly formBuilder: FormBuilder) {
		this.researcherState$
			.pipe(takeUntil(this.destroyed$))
			.subscribe((researcherState) =>
				this.formGroup.patchValue(researcherState, {emitEvent: false})
			);
		this.formGroup.valueChanges.pipe(takeUntil(this.destroyed$)).subscribe(() =>
			this.updated.emit({
				id: this.researcher.id,
				researcherState: this.formGroup.getRawValue()
			})
		);
	}

	lvlUp() {
		const next = this.formControls.currentLevel.value + 1;
		this.formControls.currentLevel.setValue(next);
	}

	lvlDown() {
		const next = Math.max(this.formControls.currentLevel.value - 1, 0);
		this.formControls.currentLevel.setValue(next);
	}

	cardsUp() {
		const next = (this.formControls.availableCards.value ?? 0) + 1;
		this.formControls.availableCards.setValue(next);
	}

	cardsDown() {
		let next = (this.formControls.availableCards.value ?? 0) - 1;
		if (next < 0) {
			next = null;
		}
		this.formControls.availableCards.setValue(next);
	}

	ngOnDestroy() {
		this.destroyed$.next();
	}
}
