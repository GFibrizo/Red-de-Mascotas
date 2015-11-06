'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:LoginService
 * @description
 * # LoginService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('LoginService', function(RequestService) {
    this.userName = "Usuario1";
    this.encryptedPassword = "123123";

    this.isLogged = function() {
    	console.log("isLogged");
        
        var requestData =  {
            method: "GET",
            url:"/login/account",
            params: {
                userName: this.userName,
                encryptedPassword: this.encryptedPassword
            }
        };
        console.log(requestData)
        RequestService.callApi(requestData).
        then(function successCallback(response) {
            alert("successCallback")
            alert(response)
          }, function errorCallback(response) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            alert("errorCallback")
            alert(response)
          });
        return false;
	}
 
    this.login = function() {
        console.log("login")
    }


    this.logout = function() {
        console.log("logout")
    }
});