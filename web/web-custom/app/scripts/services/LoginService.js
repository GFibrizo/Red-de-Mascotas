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
    

    var _data = {};
    this.data = {
        userName : "",
        password : ""
    }
    

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
                userName: this.data.userName,
                encryptedPassword: this.data.password
            }
        };
       _data = this.data;
        RequestService.callApi(requestData)
        .then(
            function successCallback(response) {
                $cookies.isLogged = true;
                if (_data.remember){
                    $cookies.userName = _data.userName;
                    $cookies.password = _data.password;
                    $cookies.remember = _data.remember;
                }
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
        delete $cookies["userName"];
        delete $cookies["password"];
        delete $cookies["remember"];
    }
});