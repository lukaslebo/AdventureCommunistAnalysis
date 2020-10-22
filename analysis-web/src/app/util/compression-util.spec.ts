import { ResearcherStateMap } from '../reducer/analysis.state';
import { Researchers } from '../reducer/researcher.state';
import {compressState, decompressState} from "./compression-util";

describe('compression', () => {
	it('should compress and decompress', () => {
		const state: ResearcherStateMap = {
			[Researchers.BigBlender.name]: {
				currentLevel: 6,
				availableCards: 2,
				nextTradeCost: null
			},
			[Researchers.ChuckClampall.name]: {
				currentLevel: 0,
				availableCards: null,
				nextTradeCost: null
			},
			[Researchers.Salvadorville.name]: {
				currentLevel: 3,
				availableCards: 0,
				nextTradeCost: 'GG'
			}
		};

		const compressed = compressState(state);
		const decompressed = decompressState(compressed);
		expect(decompressed).toEqual(state);
	});
});
