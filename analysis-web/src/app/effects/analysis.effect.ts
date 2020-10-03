import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {cookiesLoaded, loadCookies, updateAnalysisMap, updateRank, updateResearcher} from '../reducer/analysis.actions';
import {map, tap, withLatestFrom} from 'rxjs/operators';
import {AnalysisMap, ResearcherStateMap} from '../reducer/analysis.state';
import {Researchers} from '../reducer/researcher.state';
import {select, Store} from '@ngrx/store';
import {selectCurrentRank, selectResearcher} from '../reducer/analysis.reducer';
import {AnalysisService} from '../service/analysis.service';
import {CookieService} from 'ngx-cookie-service';

@Injectable()
export class AnalysisEffect {
	constructor(
		private readonly actions: Actions,
		private readonly store: Store,
		private readonly analysisService: AnalysisService,
		private readonly cookieService: CookieService
	) {
	}

	readonly loadCookie$ = createEffect(() =>
		this.actions.pipe(
			ofType(loadCookies),
			map(() => {
				let researcherStateMap: ResearcherStateMap;
				let rank: number;
				if (this.cookieService.check('GAME_STATE')) {
					const val = this.cookieService.get('GAME_STATE');
					const decompressedVal = val
						.replace(/%1/g, ',"availableCards":null')
						.replace(/%2/g, ',"availableCards":')
						.replace(/%3/g, '":{"currentLevel":');
					researcherStateMap = JSON.parse(decompressedVal);
				}
				if (this.cookieService.check('RANK')) {
					rank = JSON.parse(this.cookieService.get('RANK'));
				}
				return cookiesLoaded({rank, researcherStateMap});
			})
		)
	);

	readonly storeCookie$ = createEffect(
		() =>
			this.actions.pipe(
				ofType(updateResearcher, updateRank),
				withLatestFrom(
					this.store.pipe(select(selectResearcher)),
					this.store.pipe(select(selectCurrentRank))
				),
				tap(([action, researcherStateMap, rank]) => {
					const compressedValue = JSON.stringify(researcherStateMap)
						.replace(/,"availableCards":null/g, '%1')
						.replace(/,"availableCards":/g, '%2')
						.replace(/":\{"currentLevel":/g, '%3');
					const expires = new Date();
					expires.setFullYear(expires.getFullYear() + 3);
					this.cookieService.set(
						'GAME_STATE',
						compressedValue,
						expires,
						'/',
						document.domain,
						false,
						'Lax'
					);
					this.cookieService.set(
						'RANK',
						JSON.stringify(rank),
						expires,
						'/',
						document.domain,
						false,
						'Lax'
					);
				})
			),
		{
			dispatch: false
		}
	);

	readonly updateAnalysis$ = createEffect(() =>
		this.actions.pipe(
			ofType(updateResearcher, cookiesLoaded),
			withLatestFrom(this.store.pipe(select(selectResearcher))),
			map(([_, researcherStateMap]) => {
				const analysisMap = Researchers.allResearchers
					.map((r) => ({
						[r.id]: this.analysisService.analyze(r, researcherStateMap)
					}))
					.reduce((acc, next) => ({...acc, ...next}), <AnalysisMap>{});

				return updateAnalysisMap({analysisMap});
			})
		)
	);
}
