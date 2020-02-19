import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  branchValues: string[] = ['Mathematic', 'Serbian', 'Eglish'];
  loginForm = this.formBuilder.group({name: ['', Validators.required],
                                      surname: ['', Validators.required],
                                      city: ['', Validators.required],
                                      country: ['', Validators.required],
                                      title: [''],
                                      selectedBranches: [null],
                                      username: ['', Validators.required],
                                      reviewerFlag: [false],
                                      email: ['', [ Validators.required, Validators.email]],
                                      password: ['', [Validators.required,
                                                     Validators.minLength(8),
                                                     Validators.maxLength(50)]] });

  loginError = '';

  constructor(private route: ActivatedRoute, private formBuilder: FormBuilder) {
   }
  ngOnInit() {
  }

  login(submittedForm: FormGroup) {
    console.log(submittedForm.get('selectedBranches').value);
  }
  getErrorMessage() {
    const errorSend = '';
   /* if ( this.search.hasError('minLength')) {
      errorSend = 'Type more than 1 ch';
    }*/
    return errorSend;
  }
}
