function validateSedaAlertsForm() {

 var  env= document.getElementById("env").value;
 var  logicalDate = document.getElementById("logicalDate").value;
 var  alertIds=  document.getElementById("alertIds").value;
 var  chkAllAlerts = document.getElementById("chkAllAlerts").checked;
 var  sedaPassword = document.getElementById("sedaPassword").value;

if( env == 'prod-edc' || env =='prod-eagan'){
       if(sedaPassword.length == "" ){
           alert("password required to trigger alert's on production environment");
           document.getElementById("sedaPassword").focus();
          return false;
       }else if(sedaPassword.length > 0 ){
          if(sedaPassword != "web"){
           alert("password not matched !")
           document.getElementById("sedaPassword").focus();
           return false;
          }
       }
  }
  if (logicalDate == null || logicalDate == "") {
        alert("Logical Date Required!");
        return false;
    }if(logicalDate.length != 10){
        alert("Invalid date, date format should be YYYY-MM-DD");
        return false;
    }
  if( env !=null || env != ""){
    if((alertIds == null || alertIds== "") && chkAllAlerts==false){
        alert("Enter alert Id's that to be process");
        return false;
    }if ( (alertIds.length) > 0 && chkAllAlerts == false) {
               var formatStatus= validate(alertIds.trim());
                                 if(formatStatus){
                                 return true;
                                 }else{
                                   alert("Invalid Input, Allowed only numeric values separated  by comma (,) ");
                                   return false;
                                   }

    }
    if((alertIds.length >0) && chkAllAlerts){
        alert("Enter either alerts Ids or Click on check box to process for all users");
        return false;
    }
 }
    if( (alertIds ==null || alertIds== "") && chkAllAlerts){
     var value = confirm("Are you sure you want to trigger alerts to all users (external+internal)?");
      if(value){
        return true;
      }else{
        return false;
      }

    }
  }

 function validate(input) {
            var pattern1 = /^[0-9]{1}[0-9,]+[0-9]{1}$/g,
            pattern2 = /,{2}/g;
            return pattern1.test(input) && !pattern2.test(input);
        }

 function disableAlertIdsTextArea(){
      var  isAllAlertsSelected = document.getElementById("chkAllAlerts").checked;
          if(isAllAlertsSelected)
             document.getElementById("alertIds").disabled = true;
         else
             document.getElementById("alertIds").disabled = false;
 }
 function disableAllUsersCheckBox(){
      var  alertIds=  document.getElementById("alertIds").value;
      if(alertIds.length> 0){
            document.getElementById("chkAllAlerts").disabled = true;
      }else{
            document.getElementById("chkAllAlerts").disabled = false;
      }

 }
