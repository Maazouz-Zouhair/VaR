import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { provideRouter } from '@angular/router';

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(HttpClientModule, ReactiveFormsModule),
    provideRouter([])
  ]
})
.catch(err => console.error(err));
