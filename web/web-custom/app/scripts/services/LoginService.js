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
    

    var _data = {
      original:{},
      passwordSalt: ""
    };
    this.data = {
        userName : "",
        password : "",

    }
    

    this.isLogged = function() {
        return (!angular.isUndefined($cookies.isLogged) && ($cookies.isLogged));
	}
 
    this.login = function() {
        console.log("login");
        var deferred  = $q.defer();
        var requestData =  {
            method: "GET",
            url: "/user/" + this.data.userName + "/salt"
        };
       _data.original = this.data;
       //Obtengo el task
        RequestService.callApi(requestData)
        .then(
            function successCallback(response) {
                _data.passwordSalt = response + _data.original.password;
                var requestDataSession =  {
                    method: "GET",
                    url:"/login/account",
                    params: {
                        userName: _data.original.userName,
                        encryptedPassword:  _data.passwordSalt
                    }
                };
                //Valido sesion
                 RequestService.callApi(requestDataSession)
                    .then(
                        function successCallback(response) {
                            $cookies.isLogged = true;
                            if (_data.remember){
                                $cookies.userName = _data.original.userName;
                                $cookies.password = _data.original.password;
                                $cookies.remember = _data.original.remember;
                            }
                            deferred.resolve(response);     
                        },
                        function errorCallback(response) {
                            deferred.reject(response);
                        }            
                    );
                
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