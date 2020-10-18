import { Researchers } from './researcher.state';

describe('Researchers', () => {
	it('should have unique ids counting from 1 to N', () => {
		const ids: Array<number> = [];
		const assumedIds = Array.from(
			Array(Object.values(Researchers).length).keys()
		).map((i) => i + 1);
		Object.values(Researchers).forEach((r) => {
			if (ids.indexOf(r.id) > -1) {
				throw new Error(`Researcher id ${r.id} is not unique`);
			}
			const i = assumedIds.indexOf(r.id);
			if (i === -1) {
				throw new Error(`Researcher id ${r.id} is not expected`);
			} else {
				assumedIds.splice(i, 1);
			}

			ids.push(r.id);
		});

		if (assumedIds.length > 0) {
			throw new Error(`The assumed ids ${assumedIds} have not been used`);
		}
	});
});
