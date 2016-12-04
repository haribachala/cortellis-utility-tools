function validateRecordServicesForm() {

 var  componentName= document.forms["recordServicesForm"]["componentName"].value;
 var  task = document.forms["recordServicesForm"]["task"].value;
 var  recordServiceEnv=  document.forms["recordServicesForm"]["recordServiceEnv"].value;
 var  snapshotTime = document.forms["recordServicesForm"]["snapshotTime"].value;
 var  rsPassword = document.forms["recordServicesForm"]["rsPassword"].value;

 if(componentName=="" || componentName==null){
 alert(componentName);
    alert("Please select component name !")
    document.forms["recordServicesForm"]["componentName"].focus();
    return false;
 } if(task=="" || task==null){
     alert("Please select task name !")
     document.forms["recordServicesForm"]["task"].focus();
     return false;
  }

  if(task=='load' || task=='publish' ){

              if(snapshotTime.length < 20 || snapshotTime.length > 22 ){
                     alert("Invalid snapshot timestamp , allowed format is 'YYYY-MM-DDTHH:MM:SSZ' or 'YYYY-MM-DDTHH:MM:SSMZ'")
                     document.forms["recordServicesForm"]["snapshotTime"].focus();
                     return false;

              }else{
                       return false;
              }
  }

   if(recordServiceEnv== "" || recordServiceEnv==null){
            alert("Please select environment name !")
            document.forms["recordServicesForm"]["recordServiceEnv"].focus();
            return false;
  }
  if( recordServiceEnv.include('prod')){
           if(rsPassword.length == "" ){
               alert("password required to do any action on production environment");
               document.forms["recordServicesForm"]["rsPassword"].focus();
              return false;
           }else if(rsPassword.length > 0 ){
              if(rsPassword != "web"){
               alert("password not matched !")
               document.forms["recordServicesForm"]["rsPassword"].focus();
               return false;
              }
           }
      }








  }



