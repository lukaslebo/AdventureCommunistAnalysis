import {createAction, props} from '@ngrx/store';
import {AnalysisMap, ResearcherState, ResearcherStateMap} from './analysis.state';

export const updateRank = createAction(
	'[Analysis] updateRank',
	props<{ rank: number }>()
);

export interface UpdateResearcherProps {
	id: string;
	researcherState: ResearcherState;
}

export const updateResearcher = createAction(
	'[Analysis] updateResearcher',
	props<UpdateResearcherProps>()
);

export const updateAnalysisMap = createAction(
	'[Analysis] updateAnalysisMap',
	props<{ analysisMap: AnalysisMap }>()
);

export const loadCookies = createAction(
	'[Analysis] loadStateFromCookie'
);

export const cookiesLoaded = createAction(
	'[Analysis] cookiesLoaded',
	props<{
		rank: number,
		researcherStateMap: ResearcherStateMap
	}>()
)
