import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
	copyToClipboard,
	loadCookies,
	loadFromRoute,
	stateLoaded,
	updateAnalysisMap,
	updateRank,
	updateResearcher
} from '../reducer/analysis.actions';
import { map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { AnalysisMap, ResearcherStateMap } from '../reducer/analysis.state';
import { Researchers } from '../reducer/researcher.state';
import { select, Store } from '@ngrx/store';
import {
	selectCurrentRank,
	selectResearcher
} from '../reducer/analysis.reducer';
import { AnalysisService } from '../service/analysis.service';
import { CookieService } from 'ngx-cookie-service';
import { Clipboard } from '@angular/cdk/clipboard';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { compressState, decompressState } from '../util/compression-util';

@Injectable()
export class AnalysisEffect {
	constructor(
		private readonly actions: Actions,
		private readonly store: Store,
		private readonly analysisService: AnalysisService,
		private readonly cookieService: CookieService,
		private readonly clipboard: Clipboard,
		private readonly router: Router
	) {}

	readonly loadCookie$ = createEffect(() =>
		this.actions.pipe(
			ofType(loadCookies),
			map(() => {
				let researcherStateMap: ResearcherStateMap;
				let rank: number;
				if (this.cookieService.check('GAME_STATE')) {
					const gameState = this.cookieService.get('GAME_STATE');
					researcherStateMap = decompressState(gameState);
				}
				if (this.cookieService.check('RANK')) {
					rank = JSON.parse(this.cookieService.get('RANK'));
				}
				return stateLoaded({ rank, researcherStateMap });
			})
		)
	);

	readonly storeCookie$ = createEffect(
		() =>
			this.actions.pipe(
				ofType(updateResearcher, updateRank, stateLoaded),
				withLatestFrom(
					this.store.pipe(select(selectResearcher)),
					this.store.pipe(select(selectCurrentRank))
				),
				tap(([_, researcherStateMap, rank]) => {
					const expires = new Date();
					expires.setFullYear(expires.getFullYear() + 3);
					this.cookieService.set(
						'GAME_STATE',
						compressState(researcherStateMap),
						expires,
						'/',
						document.domain,
						false,
						'Lax'
					);

					this.cookieService.set(
						'RANK',
						rank?.toString(),
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
			ofType(updateResearcher, stateLoaded),
			withLatestFrom(this.store.pipe(select(selectResearcher))),
			map(([_, researcherStateMap]) => {
				const analysisMap = Researchers.allResearchers
					.map((r) => ({
						[r.name]: this.analysisService.analyze(r, researcherStateMap)
					}))
					.reduce((acc, next) => ({ ...acc, ...next }), <AnalysisMap>{});

				return updateAnalysisMap({ analysisMap });
			})
		)
	);

	readonly loadStateFromRoute$ = createEffect(() =>
		this.actions.pipe(
			ofType(loadFromRoute),
			switchMap(({ rank, encodedState }) =>
				of(
					stateLoaded({
						rank,
						researcherStateMap: decompressState(encodedState)
					})
				)
			),
			tap(() => this.router.navigate(['/']))
		)
	);

	readonly copyToClipboard$ = createEffect(
		() =>
			this.actions.pipe(
				ofType(copyToClipboard),
				withLatestFrom(
					this.store.pipe(select(selectCurrentRank)),
					this.store.pipe(select(selectResearcher))
				),
				tap(([_, rank, researcherStateMap]) => {
					this.clipboard.copy(
						window.location.origin +
							window.location.pathname +
							`?r=${rank}&s=${encodeURIComponent(
								compressState(researcherStateMap)
							)}`
					);
				})
			),
		{ dispatch: false }
	);
}
