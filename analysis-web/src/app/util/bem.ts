/**
 * BEM - Block Element Modifier helper function
 */
export function BEM(blockName: string) {
	return (elementName: string, ...modifier: string[]) => {
		let styleClasses = blockName;

		if (elementName) {
			styleClasses += '__' + elementName;
		}

		if (modifier?.length > 0) {
			const base = styleClasses;
			styleClasses +=
				' ' + modifier.map((modifier) => base + '--' + modifier).join(' ');
		}
		return styleClasses;
	};
}
