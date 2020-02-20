import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.modules';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegistrationComponent } from './registration/registration.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NewMagazineComponent } from './new-magazine/new-magazine.component';
import { RegisterComponent } from './register/register.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { ApproveReviewerComponent } from './approve-reviewer/approve-reviewer.component';
import { LoginComponent } from './login/login.component';
import { HomepageComponent } from './homepage/homepage.component';
import { ActivationComponent } from './activation/activation.component';
import { FillMagazineComponent } from './fill-magazine/fill-magazine.component';
import { CheckingMagazineComponent } from './checking-magazine/checking-magazine.component';
import { JwtInterceptor } from './jwt.interceptor';
import { NewTextComponent } from './new-text/new-text.component';
import { FillTextComponent } from './fill-text/fill-text.component';
import { CheckThemeComponent } from './check-theme/check-theme.component';
import { AddCoauthorComponent } from './add-coauthor/add-coauthor.component';
import { FillCoauthorComponent } from './fill-coauthor/fill-coauthor.component';
import { CheckFormatComponent } from './check-format/check-format.component';
import { ChangePdfComponent } from './change-pdf/change-pdf.component';
import { SelectReviewerComponent } from './select-reviewer/select-reviewer.component';
import { PayComponent } from './pay/pay.component';
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    NewMagazineComponent,
    RegisterComponent,
    ApproveReviewerComponent,
    LoginComponent,
    HomepageComponent,
    ActivationComponent,
    FillMagazineComponent,
    CheckingMagazineComponent,
    NewTextComponent,
    FillTextComponent,
    CheckThemeComponent,
    AddCoauthorComponent,
    FillCoauthorComponent,
    CheckFormatComponent,
    ChangePdfComponent,
    SelectReviewerComponent,
    PayComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MaterialModule,
    FlexLayoutModule,
    HttpClientModule,
    HttpModule
  ],
  providers: [ {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
