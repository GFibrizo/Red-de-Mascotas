'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('DenunciationCtrl', function($scope,$position,$state) {
      console.log("DenunciationCtrl")
      
      

      $scope.showMessageAccept = function(){        
        console.log("showMessageAccept")
        alert("Aceptar denuncia")
      }

      $scope.showMessage = function(){        
  		  console.log("Denuncias")
        alert("Rechazar denuncia")
      }
  	
  });
