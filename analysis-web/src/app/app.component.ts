import {Component} from '@angular/core';
import {BEM} from './util/bem';
import {REM} from './util/rem';
import {Store} from '@ngrx/store';

@Component({
	selector: 'analysis-app',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	readonly blockName = 'analysis-app';
	readonly styles = BEM(this.blockName);
	readonly dimensions = REM();

	constructor(private readonly store: Store) {
	}
}