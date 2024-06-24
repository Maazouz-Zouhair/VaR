import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VaRService } from '../services/var.service';
import { VaRRequest } from '../models/var-request.model';
import { VaRResponse } from '../models/var-response.model';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  uploadForm: FormGroup;
  valueAtRisk: number | undefined;
  errorMessage: string | undefined;
  fileError: string | undefined;

  constructor(private fb: FormBuilder, private varService: VaRService) {
    this.uploadForm = this.fb.group({
      file: [null, Validators.required],
      confidenceLevel: [0.95, [Validators.required, Validators.min(0), Validators.max(1)]],
      calculationType: ['single', Validators.required]
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      if (file.size === 0) {
        this.fileError = 'File is empty. Please select a valid CSV file.';
        this.uploadForm.patchValue({ file: null });
      } else if (!file.name.endsWith('.csv')) {
        this.fileError = 'Invalid file type. Please select a CSV file.';
        this.uploadForm.patchValue({ file: null });
      } else {
        this.fileError = undefined;
        this.uploadForm.patchValue({ file: file });
      }
      this.uploadForm.get('file')?.updateValueAndValidity();
    }
  }

  onSubmit() {
    this.valueAtRisk = undefined;
    this.errorMessage = undefined;
    this.fileError = undefined;

    if (this.uploadForm.valid) {
      const formValues = this.uploadForm.value;
      const request: VaRRequest = {
        file: formValues.file,
        confidenceLevel: formValues.confidenceLevel
      };

      if (formValues.calculationType === 'single') {
        this.calculateSingleTradeVaR(request);
      } else if (formValues.calculationType === 'portfolio') {
        this.calculatePortfolioVaR(request);
      } else {
        this.errorMessage = 'Invalid calculation type';
      }
    } else {
      this.errorMessage = 'Please provide a valid file and confidence level';
    }
  }

  calculateSingleTradeVaR(request: VaRRequest) {
    this.varService.calculateSingleTradeVaR(request).subscribe(
      (response: VaRResponse) => {
        this.valueAtRisk = response.valueAtRisk;
      },
      (error: string) => {
        this.errorMessage = error;
      }
    );
  }

  calculatePortfolioVaR(request: VaRRequest) {
    this.varService.calculatePortfolioVaR(request).subscribe(
      (response: VaRResponse) => {
        this.valueAtRisk = response.valueAtRisk;
      },
      (error: string) => {
        this.errorMessage = error;
      }
    );
  }
}
