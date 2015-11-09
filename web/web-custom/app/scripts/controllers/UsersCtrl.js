'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('UsersCtrl', function($scope,$position,$state,$cookies,UsersService) {
      console.log("UsersCtrl")
      
      $scope.data = {
        users:[]
      };
      UsersService.getUsers().then(
        function succes(response){
          $scope.data = response.data;
        }
      );
  	
  });
