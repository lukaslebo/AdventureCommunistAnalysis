import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {updateAnalysisMap, updateResearcher} from "../reducer/analysis.actions";
import {map, withLatestFrom} from "rxjs/operators";
import {AnalysisMap} from "../reducer/analysis.state";
import {Researchers} from "../reducer/researcher.state";
import {select, Store} from "@ngrx/store";
import {selectResearcher} from "../reducer/analysis.reducer";
import {AnalysisService} from "../service/analysis.service";


@Injectable()
export class AnalysisEffect {

	constructor(
		private readonly actions: Actions,
		private readonly store: Store,
		private readonly analysisService: AnalysisService
	) {
	}

	readonly updateAnalysis$ = createEffect(() => this.actions.pipe(
		ofType(updateResearcher),
		withLatestFrom(this.store.pipe(select(selectResearcher))),
		map(([action, researcherStateMap]) => {
			const researcher = Researchers.allResearchers.find(r => r.id === action.id)

			const analysisMap = Researchers.allResearchers
				.map(r => ({[r.id]: this.analysisService.analyze(r, researcherStateMap)}))
				.reduce((acc, next) => ({...acc, ...next}), <AnalysisMap>{});

			return updateAnalysisMap({analysisMap})
		})
	));
}