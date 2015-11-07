'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('MainCtrl', function($scope,$position,$state,LoginService) {
  		
  		LoginService.isLogged()
  		.then(
  			function successCallback (argument) {
  				// body...
  			},
  			function errorCallback (response){
  				$state.go('login');
  			}
  		);
  		
  	
  });
