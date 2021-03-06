import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexModule } from '@angular/flex-layout';
import { ReactiveFormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';
import { analysisReducerMap } from './reducer/analysis.reducer';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { environment } from '../environments/environment';
import { ResearcherAnalysisRowComponent } from './components/researcher-analysis-row/researcher-analysis-row.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { EffectsModule } from '@ngrx/effects';
import { AnalysisEffect } from './effects/analysis.effect';
import { AnalysisService } from './service/analysis.service';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { ClipboardModule } from '@angular/cdk/clipboard';

@NgModule({
	declarations: [AppComponent, ResearcherAnalysisRowComponent],
	imports: [
		AppRoutingModule,
		BrowserAnimationsModule,
		BrowserModule,
		CommonModule,
		ClipboardModule,
		FlexModule,
		MatFormFieldModule,
		MatInputModule,
		MatButtonModule,
		MatCheckboxModule,
		MatIconModule,
		ReactiveFormsModule,
		StoreModule.forRoot(analysisReducerMap),
		StoreDevtoolsModule.instrument({
			maxAge: 25,
			logOnly: environment.production
		}),
		EffectsModule.forRoot([AnalysisEffect])
	],
	providers: [AnalysisService],
	bootstrap: [AppComponent]
})
export class AppModule {}
