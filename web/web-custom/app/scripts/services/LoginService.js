'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:LoginService
 * @description
 * # LoginService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('LoginService', function($q,RequestService) {
    this.userName = "Usuario3";
    this.encryptedPassword = "[B@b7ff838123123";

    this.isLogged = function() {
    	console.log("isLogged");
        var deferred  = $q.defer();
        var requestData =  {
            method: "GET",
            url:"/login/account",
            params: {
                userName: this.userName,
                encryptedPassword: this.encryptedPassword
            }
        };
        console.log(requestData)
        RequestService.callApi(requestData)
        .then(
            function successCallback(response) {
                deferred.resolve(response);     
            }, 
            function errorCallback(response) {
                deferred.reject(response);
            }
        );
        return deferred.promise;
	}
 
    this.login = function() {
        console.log("login")
    }


    this.logout = function() {
        console.log("logout")
    }
});