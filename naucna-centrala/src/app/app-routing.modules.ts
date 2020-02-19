import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from './/registration/registration.component';
import { NewMagazineComponent } from './/new-magazine/new-magazine.component';
import { RegisterComponent } from './/register/register.component';
import { ApproveReviewerComponent } from './/approve-reviewer/approve-reviewer.component';
import { LoginComponent } from './/login/login.component';
import { HomepageComponent } from './/homepage/homepage.component';
import { ActivationComponent } from './/activation/activation.component';
import { FillMagazineComponent } from './/fill-magazine/fill-magazine.component';
import { CheckingMagazineComponent } from './/checking-magazine/checking-magazine.component';
import { NewTextComponent } from './/new-text/new-text.component';
import { FillTextComponent } from './/fill-text/fill-text.component';
import { CheckThemeComponent } from './/check-theme/check-theme.component';
import { FillCoauthorComponent } from './/fill-coauthor/fill-coauthor.component';
import { AddCoauthorComponent } from './/add-coauthor/add-coauthor.component';
import { CheckFormatComponent } from './/check-format/check-format.component';
import { ChangePdfComponent } from './/change-pdf/change-pdf.component';
import { SelectReviewerComponent } from './/select-reviewer/select-reviewer.component';
import { PayComponent } from './/pay/pay.component';
const routes: Routes = [{
    path: '',
    component: LoginComponent
   },
   {
    path: 'signup',
    component: RegisterComponent
   },
   {
    path: 'newMagazine',
    component: NewMagazineComponent
   },
   {
    path: 'fillMagazine/:process_id',
    component: FillMagazineComponent
   },
   {
    path: 'checkingMagazine/:task_id',
    component: CheckingMagazineComponent
   },
   {
    path: 'approveReviewer/:process_id',
    component: ApproveReviewerComponent
   },
   {
    path: 'checkTheme/:task_id',
    component: CheckThemeComponent
   },
   {
    path: 'selectRev/:process_id',
    component: SelectReviewerComponent
   },
   {
    path: 'checkFormat/:task_id',
    component: CheckFormatComponent
   },
   {
    path: 'changePdf/:task_id',
    component: ChangePdfComponent
   },
   {
    path: 'addCoauthor/:process_id',
    component: AddCoauthorComponent
   },
   {
    path: 'fillCoauthor/:process_id',
    component: FillCoauthorComponent
   },
   {
    path: 'fillText/:process_id',
    component: FillTextComponent
   },
   {
    path: 'activate/:process_id/:username',
    component: ActivationComponent
   },
   {
    path: 'pay/:process_id',
    component: PayComponent
   },
   {
    path: 'login',
    component: LoginComponent
   },
   {
    path: 'homepage',
    component: HomepageComponent
   },
   {
    path: 'text',
    component: NewTextComponent
   }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
export class AppRoutingModule { }
