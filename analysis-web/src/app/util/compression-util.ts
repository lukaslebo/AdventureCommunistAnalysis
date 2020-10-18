import { ResearcherStateMap } from '../reducer/analysis.state';
import { Researchers } from '../reducer/researcher.state';
import { compress, decodeBase64, decompress, encodeBase64 } from 'lzutf8';

export function compressState(researcherStateMap: ResearcherStateMap) {
	let compressedValue = JSON.stringify(researcherStateMap)
		.replace(/,"availableCards":null/g, '-1')
		.replace(/,"availableCards":/g, '-2')
		.replace(/":\{"currentLevel":/g, '-3')
		.replace(/,"nextTradeCost":null/g, '-4')
		.replace(/,"nextTradeCost":"/g, '-5')
		.replace(/"},"/g, '-6')
		.replace(/},"/g, '-7')
		.replace(/\{"/g, '-8')
		.replace(/}}/g, '-9');
	Researchers.allResearchers.forEach((r, i) => {
		compressedValue = compressedValue.replace(r.name, `_${r.id}`);
	});

	compressedValue = encodeBase64(compress(compressedValue));
	return compressedValue;
}

export function decompressState(compressedState: string) {
	let decompressedState = decompress(decodeBase64(compressedState));

	decompressedState = decompressedState
		.replace(/-1/g, ',"availableCards":null')
		.replace(/-2/g, ',"availableCards":')
		.replace(/-3/g, '":{"currentLevel":')
		.replace(/-4/g, ',"nextTradeCost":null')
		.replace(/-5/g, ',"nextTradeCost":"')
		.replace(/-6/g, '"},"')
		.replace(/-7/g, '},"')
		.replace(/-8/g, '{"')
		.replace(/-9/g, '}}');
	Researchers.allResearchers.forEach((r) => {
		decompressedState = decompressedState.replace(`_${r.id}`, r.name);
	});

	return JSON.parse(decompressedState);
}
