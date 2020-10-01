const REM_PX_RATIO = 16;

export function REM() {
	return (px: number) => px / REM_PX_RATIO + 'rem';
}
