import {ActionReducerMap, createFeatureSelector, createReducer, on} from '@ngrx/store';
import {AnalysisMap, AnalysisRootState, ResearcherStateMap} from './analysis.state';
import {Researchers} from './researcher.state';
import {cookiesLoaded, updateAnalysisMap, updateRank, updateResearcher} from './analysis.actions';
import produce from 'immer';

const rankReducer = createReducer(
	1,
	on(updateRank, (_, {rank}) => rank),
	on(cookiesLoaded, (state, {rank}) => rank ?? state)
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
const researcherReducer = createReducer(
	researcherInitialState,
	on(updateResearcher, (state, {id, researcherState}) =>
		produce(state, (draft: ResearcherStateMap) => {
			draft[id] = researcherState;
		})
	),
	on(cookiesLoaded, (state, {researcherStateMap}) => ({
		...state,
		...researcherStateMap
	}))
);

const analysisInitialState = Researchers.allResearchers.reduce(
	(acc, r) => ({
		...acc,
		[r.id]: null
	}),
	<AnalysisMap>{}
);

const analysisReducer = createReducer(
	analysisInitialState,
	on(updateAnalysisMap, (state, {analysisMap}) => ({
		...state,
		...analysisMap
	}))
);

const cookieReducer = createReducer(
	false,
	on(cookiesLoaded, () => true)
);

export const analysisReducerMap: ActionReducerMap<AnalysisRootState> = {
	currentRank: rankReducer,
	researcher: researcherReducer,
	analysis: analysisReducer,
	cookiesLoaded: cookieReducer
};

export const selectCurrentRank = createFeatureSelector<AnalysisRootState,
	number>('currentRank');

export const selectResearcher = createFeatureSelector<AnalysisRootState,
	ResearcherStateMap>('researcher');

export const selectAnalysis = createFeatureSelector<AnalysisRootState,
	AnalysisMap>('analysis');

export const selectCookiesLoaded = createFeatureSelector<AnalysisRootState,
	boolean>('cookiesLoaded');
