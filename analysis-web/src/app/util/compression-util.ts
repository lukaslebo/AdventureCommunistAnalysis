import { ResearcherState, ResearcherStateMap } from '../reducer/analysis.state';
import { Researchers } from '../reducer/researcher.state';
import { compress, decodeBase64, decompress, encodeBase64 } from 'lzutf8';
import { unitsMapping } from './human-readable-numbers';

// TODO remove v0 encoding after a couple weeks

export function compressState(researcherStateMap: ResearcherStateMap) {
	return shift(encode_v1(researcherStateMap));
}

export function decompressState(compressedState: string) {
	if (isBase64Encoded(compressedState)) {
		const decompressedString = stringDecompress(compressedState);
		if (matches_v0_encodingPattern(decompressedString)) {
			return decode_v0(decompressedString);
		}
	}
	return decode_v1(unshift(compressedState));
}

function encode_v0(state: ResearcherStateMap) {
	const json = JSON.stringify(state);
	let encodedJson = json
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
		encodedJson = encodedJson.replace(r.name, `_${r.id}`);
	});

	return encodedJson;
}

function isBase64Encoded(input: string) {
	return /^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$/.test(
		input
	);
}

function matches_v0_encodingPattern(input: string) {
	return /((_[0-9]+)|(-[1-9]))+/.test(input);
}

function decode_v0(encodedJson: string) {
	let decodedJson = encodedJson
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
		decodedJson = decodedJson.replace(`_${r.id}`, r.name);
	});

	return JSON.parse(decodedJson);
}

function stringCompress(value: string) {
	return encodeBase64(compress(value));
}

function stringDecompress(value: string) {
	return decompress(decodeBase64(value));
}

function encode_v1(state: ResearcherStateMap) {
	let encodedJson = '';
	Object.keys(state).forEach((name) => {
		let part = '';
		const researcherState = state[name];
		const researcher = Researchers.allResearchers.find((r) => r.name === name);
		part += toBase62(researcher.id).padStart(2, '0');
		part += toBase62(researcherState.currentLevel);
		if (researcher.modifier === 'Trade' && researcher.rarity !== 'Supreme') {
			if (researcherState.nextTradeCost == null) {
				part += '00';
			} else {
				part += toBase62(
					unitsMapping.indexOf(researcherState.nextTradeCost) + 1
				).padStart(2, '0');
			}
		}
		if (researcherState.availableCards != null) {
			part += toBase62(researcherState.availableCards);
		}
		const size = toBase62(part.length + 1);
		encodedJson += size + part;
	});
	return encodedJson;
}

function decode_v1(encodedJson: string) {
	let remaining = encodedJson;
	const researcherStateMap: ResearcherStateMap = {};
	while (remaining.length > 0) {
		const size = toBase10(remaining.substr(0, 1));
		const state: ResearcherState = {
			currentLevel: 0,
			availableCards: null,
			nextTradeCost: null
		};
		let part = remaining.substr(1, size - 1);
		remaining = remaining.substr(size);
		const id = toBase10(part.substr(0, 2));
		const researcher = Researchers.allResearchers.find((r) => r.id === id);
		part = part.substr(2);
		const lvl = toBase10(part.substr(0, 1));
		part = part.substr(1);
		state.currentLevel = lvl;
		if (researcher.modifier === 'Trade' && researcher.rarity !== 'Supreme') {
			const tradecost = part.substr(0, 2);
			part = part.substr(2);
			if (tradecost !== '00') {
				state.nextTradeCost = unitsMapping[toBase10(tradecost) - 1];
			}
		}
		if (part.length > 0) {
			const availableCards = toBase10(part);
			state.availableCards = availableCards;
		}
		researcherStateMap[researcher.name] = state;
	}
	return researcherStateMap;
}

function toBase10(numString: string) {
	let power = 1;
	let num = 0;
	for (let i = numString.length - 1; i >= 0; i--) {
		const char = numString.charAt(i);
		if (base62Symbols.indexOf(char) === -1) {
			return -1;
		}
		num += base62Symbols.indexOf(char) * power;
		power = power * 62;
	}
	return num;
}

function toBase62(num: number) {
	let s = '';
	while (num > 0) {
		s = base62Symbols[num % 62] + s;
		num = Math.floor(num / 62);
	}

	return s;
}

const base62Symbols = createBase62Symbols();

function createBase62Symbols() {
	const chars: string[] = [];
	for (let i = 0; i <= 9; i++) {
		chars.push(i.toString());
	}
	for (let i = 97; i <= 122; i++) {
		chars.push(String.fromCharCode(i));
	}
	for (let i = 65; i <= 90; i++) {
		chars.push(String.fromCharCode(i));
	}
	return chars;
}

function shift(input: string) {
	let res = '';
	input.split('').forEach((c, i) => {
		const j = base62Symbols.indexOf(c);
		const delta = 47 * (i + 4) + ((i + 4) % 39);
		res += base62Symbols[(j + delta) % 62];
	});
	return res;
}

function unshift(input: string) {
	let res = '';
	input.split('').forEach((c, i) => {
		const j = base62Symbols.indexOf(c);
		const delta = 47 * (i + 4) + ((i + 4) % 39);
		let y = (j - delta) % 62;
		if (y === -0) {
			y = 0;
		} else if (y < 0) {
			y += 62;
		}
		res += base62Symbols[y];
	});
	return res;
}
