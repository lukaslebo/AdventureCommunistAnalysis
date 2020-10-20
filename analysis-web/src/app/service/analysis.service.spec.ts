import { TestBed } from '@angular/core/testing';
import { AnalysisService } from './analysis.service';
import { Researchers } from '../reducer/researcher.state';
import { ResearcherStateMap } from '../reducer/analysis.state';
import { Decimal } from 'decimal.js';

describe('analysis', () => {
	let analysisService = null;
	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [AnalysisService]
		});
		analysisService = TestBed.inject(AnalysisService);
	});

	it('should compute meaningful chance boost', () => {
		// Placebo: 5 Commons
		// Supreme Chance Lvl 0
		// Industry Chance Lvl 4
		// Supreme Bonus Multiplier Lvl 2
		// Industry Bonus Multiplier Lvl 4
		const state: ResearcherStateMap = {
			[Researchers.KennieWhooser.name]: {
				currentLevel: 1,
				availableCards: null
			},
			[Researchers.Patcheye.name]: {
				currentLevel: 1,
				availableCards: null
			},
			[Researchers.JDMD.name]: {
				currentLevel: 1,
				availableCards: null
			},
			[Researchers.HowlinMac.name]: {
				currentLevel: 1,
				availableCards: null
			},
			[Researchers.EleanorLynn.name]: {
				currentLevel: 1,
				availableCards: null
			},
			[Researchers.NurseTemple.name]: {
				currentLevel: 4,
				availableCards: null
			},
			[Researchers.HannibalTavius.name]: {
				currentLevel: 4,
				availableCards: null
			},
			[Researchers.DrShortstack.name]: {
				currentLevel: 0,
				availableCards: null
			},
			[Researchers.AlfStark.name]: {
				currentLevel: 2,
				availableCards: null
			}
		};

		const analysis = analysisService.analyze(Researchers.NurseTemple, state);
		expect(parseFloat(analysis.boost)).toBeCloseTo(1.684);
	});
});
