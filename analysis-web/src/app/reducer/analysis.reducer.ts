import {ActionReducerMap, createFeatureSelector, createReducer, on} from '@ngrx/store';
import {AnalysisMap, AnalysisRootState, ResearcherStateMap} from './analysis.state';
import {Researchers} from './researcher.state';
import {updateAnalysisMap, updateRank, updateResearcher} from './analysis.actions';
import produce from 'immer';

export const rankReducer = createReducer(
	1,
	on(updateRank, (_, {rank}) => rank)
);

export const initialResearcherState = {
	currentLevel: 0,
	availableCards: null
};
const researcherInitialState = Researchers.allResearchers.reduce(
	(acc, r) => ({
		...acc,
		[r.id]: initialResearcherState
	}),
	<ResearcherStateMap>{}
);
export const researcherReducer = createReducer(
	researcherInitialState,
	on(updateResearcher, (state, {id, researcherState}) =>
		produce(state, (draft: ResearcherStateMap) => {
			draft[id] = researcherState;
		})
	)
);

const analysisInitialState = Researchers.allResearchers.reduce(
	(acc, r) => ({
		...acc,
		[r.id]: null
	}),
	<AnalysisMap>{}
);
export const analysisReducer = createReducer(
	analysisInitialState,
	on(updateAnalysisMap, (state, {analysisMap}) => ({
		...state,
		...analysisMap
	}))
);

export const analysisReducerMap: ActionReducerMap<AnalysisRootState> = {
	currentRank: rankReducer,
	researcher: researcherReducer,
	analysis: analysisReducer
};

export const selectCurrentRank = createFeatureSelector<AnalysisRootState,
	number>('currentRank');

export const selectResearcher = createFeatureSelector<AnalysisRootState,
	ResearcherStateMap>('researcher');

export const selectAnalysis = createFeatureSelector<AnalysisRootState,
	AnalysisMap>('analysis');
