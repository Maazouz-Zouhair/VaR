import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { VaRRequest } from '../models/var-request.model';
import { VaRResponse } from '../models/var-response.model';

@Injectable({
  providedIn: 'root'
})
export class VaRService {
  private apiUrl = 'http://localhost:8080/api/var';

  constructor(private http: HttpClient) { }

  calculateVaR(request: VaRRequest): Observable<VaRResponse> {
    const formData = new FormData();
    formData.append('file', request.file);
    formData.append('confidenceLevel', request.confidenceLevel.toString());

    return this.http.post<VaRResponse>(`${this.apiUrl}/calculate`, formData)
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      console.log(error)
      // A client-side or network error occurred.
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // The backend returned an unsuccessful response code.
      // Extract the message from the response body
      if (error.error && error.error.message) {
        errorMessage = error.error.message;
      } else {
        if(error.error.confidenceLevel)
        errorMessage = `Error: ${error.error.confidenceLevel}`;
      else if(error.error.historicalValues)
        errorMessage = `Error: ${error.error.confidenceLevel}`;
        else 
        errorMessage = `Error: ${error.error}`;
      }
    }
    return throwError(errorMessage);
  }
}
