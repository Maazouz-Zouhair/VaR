import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { UploadComponent } from './upload/upload.component';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, UploadComponent],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'VaR Calculator';
}
