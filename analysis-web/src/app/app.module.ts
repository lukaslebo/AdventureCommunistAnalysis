import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexModule} from '@angular/flex-layout';
import {ReactiveFormsModule} from '@angular/forms';
import {StoreModule} from '@ngrx/store';
import {analysisReducerMap} from './reducer/analysis.reducer';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {environment} from '../environments/environment';
import {ResearcherAnalysisRowComponent} from './components/researcher-analysis-row/researcher-analysis-row.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from "@angular/material/icon";

@NgModule({
	declarations: [AppComponent, ResearcherAnalysisRowComponent],
	imports: [
		AppRoutingModule,
		BrowserAnimationsModule,
		BrowserModule,
		FlexModule,
		MatFormFieldModule,
		MatInputModule,
		MatIconModule,
		ReactiveFormsModule,
		StoreModule.forRoot(analysisReducerMap),
		StoreDevtoolsModule.instrument({
			maxAge: 25,
			logOnly: environment.production
		})
	],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule {
}
