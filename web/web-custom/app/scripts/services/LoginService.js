'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:LoginService
 * @description
 * # LoginService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('LoginService', function($q,$cookies,RequestService) {
    this.userName = "Usuario1";
    this.encryptedPassword = "[B@b7ff838123123";

    this.isLogged = function() {
        return (!angular.isUndefined($cookies.isLogged) && ($cookies.isLogged));
	}
 
    this.login = function() {
        console.log("login");
        var deferred  = $q.defer();
        var requestData =  {
            method: "GET",
            url:"/login/account",
            params: {
                userName: this.userName,
                encryptedPassword: this.encryptedPassword
            }
        };
       
        RequestService.callApi(requestData)
        .then(
            function successCallback(response) {
                $cookies.isLogged = true;
                deferred.resolve(response);     
            }, 
            function errorCallback(response) {
                deferred.reject(response);
            }
        );
        return deferred.promise;
    }


    this.logout = function() {
        console.log("logout")
        delete $cookies["isLogged"];
    }
});