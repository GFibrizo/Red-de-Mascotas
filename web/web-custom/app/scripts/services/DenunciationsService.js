'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:DenunciationsService
 * @description
 * # DenunciationsService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('DenunciationsService', function($http,$q ,RequestService) {

    
    /*Atributes*/
    
    /*Functions*/

    this.getDenunciations = function(filters) {
        var deferred  = $q.defer();
        var requestData =  {
            method: "GET",
            url: "/pets/denunciations"
        };
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

    /*
        PUT    /pet/{petId}/report/accepted
        Header: application/json
        Body de ejemplo:
        {
        "petId": "5646013b44ae767f48567d7c",
        "informer": "5645e44e44ae767f48567d7a",
        "publicationType": ""
        }
    */
    this.acceptDenunciation = function(object) {
        var deferred  = $q.defer();
        var requestData =  {
            method: "PUT",
            url: "/pet/" + object.publication.publicationId + "/report/accepted",
            params: {
                petId: object.publication.publicationId,
                informer:  object.complainantUserId,
                publicationType: object.publication.type,
            }
        };
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


    /*  PUT    /pet/{petId}/report/rejected
        Header: application/json
        Body de ejemplo:
        {
        "petId": "5646013b44ae767f48567d7c",
        "informer": "5645e44e44ae767f48567d7a"
        "publicationType": ""
        }
    */
    this.rejectDenunciation = function(object) {
        var deferred  = $q.defer();
        var requestData =  {
            method: "PUT",
            url: "/pet/" + object.publication.publicationId + "/report/rejected",
            params: {
                petId: object.publication.publicationId,
                informer:  object.complainantUserId,
                publicationType: object.publication.type
            }
        };
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
 
});