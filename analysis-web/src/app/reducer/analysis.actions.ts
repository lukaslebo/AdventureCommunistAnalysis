import {createAction, props} from '@ngrx/store';
import {AnalysisMap, ResearcherState} from './analysis.state';

export const updateRank = createAction('updateRank', props<{ rank: number }>());

export interface UpdateResearcherProps {
	id: string;
	researcherState: ResearcherState;
}

export const updateResearcher = createAction(
	'updateResearcher',
	props<UpdateResearcherProps>()
);

export const updateAnalysisMap = createAction(
	'updateAnalysisMap',
	props<{ map: AnalysisMap }>()
);
