import themes from './assets/themes.json';

export class ThemeManager {
	static getTheme() {
		if (typeof localStorage === 'undefined') return 'light';
		if (!localStorage.getItem('theme')) localStorage.setItem('theme', 'light');
		return localStorage.getItem('theme') || 'light';
	}

	static setTheme(theme: string) {
		localStorage.setItem('theme', theme);
	}

	static getVariable(name: string) {
		// @ts-ignore
		let theme = flattenObject(themes[ThemeManager.getTheme()].styles);
		return theme[name];
	}

	static compileVariables() {
		// @ts-ignore
		let theme = flattenObject(themes[ThemeManager.getTheme()].styles);
		let css = '';

		for (let key in theme) {
			css += `--${key}: ${theme[key]};`;
		}

		return css;
	}
}

function flattenObject(obj: any, prefix: string = '') {
	let result: any = {};
	for (let key in obj) {
		if (typeof obj[key] === 'object') {
			Object.assign(result, flattenObject(obj[key], `${prefix}${key}-`));
		} else {
			result[`${prefix}${key}`] = obj[key];
		}
	}
	return result;
}
