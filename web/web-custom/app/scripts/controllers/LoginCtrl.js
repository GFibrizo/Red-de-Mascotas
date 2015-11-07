'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('LoginCtrl', function($scope,$position,$state,LoginService) {
      console.log("LoginCtrl")
      

      $scope.login = function(){        
  		  console.log("Login")
        LoginService.login()
        .then(
          function successCallback (response) {
            $state.go('dashboard.home');
          }
        );
      }
  	
  });
