import { Decimal } from 'decimal.js';

export function toHumanReadable(decimal: Decimal) {
	const powerOfThree = Math.max(
		Math.floor((decimal.precision(true) - decimal.decimalPlaces() - 1) / 3),
		0
	);
	const num = decimal
		.div(new Decimal(10).pow(powerOfThree * 3))
		.toDecimalPlaces(3);
	const suffix = unitsMapping[powerOfThree];
	return num.toString() + suffix;
}

const unitsMapping = getUnitsMapping();

function getUnitsMapping() {
	const units = ['', 'K', 'M', 'B', 'T'];
	const abc = [];
	for (let charCode = 65; charCode <= 90; charCode++) {
		abc.push(String.fromCharCode(charCode));
	}
	for (let repeat = 2; repeat <= 4; repeat++) {
		for (let char of abc) {
			units.push(''.padEnd(repeat, char));
		}
	}
	return units;
}
