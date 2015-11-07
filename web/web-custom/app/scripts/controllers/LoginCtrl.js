'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('LoginCtrl', function($scope,$position,$state,$cookies,LoginService) {
      console.log("LoginCtrl")
      
      $scope.data = {
        userName: "",
        password: "",
        remember : false
      }

      $scope.init = function (){
        $scope.data.userName = (!angular.isUndefined($cookies.userName)) ? $cookies.userName : ""; 
        $scope.data.password = (!angular.isUndefined($cookies.password)) ? $cookies.password : "";      
        $scope.data.remember = (!angular.isUndefined($cookies.remember)) ? $cookies.remember : "";      
      }

      

      $scope.login = function(){        
  		  console.log("Login")
        LoginService.data  = $scope.data;

        LoginService.login()
        .then(
          function successCallback (response) {
            $state.go('dashboard.home');
          }
        );
      }
  	
  });
