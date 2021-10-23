from django.shortcuts import render
from rest_framework_simplejwt.tokens import RefreshToken, TokenError
from rest_framework import serializers
from django.http import HttpResponse
from rest_framework.response import Response
from .models import LostObject, LostItem, LostPerson, LostPersonImage
from rest_framework import status
from rest_framework import status, filters
from .serializers import *
from rest_framework import generics
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.reverse import reverse
from rest_framework import status
from rest_framework import viewsets
from rest_framework import permissions
from rest_framework.decorators import api_view
from django_filters.rest_framework import DjangoFilterBackend
from rest_framework.filters import SearchFilter, OrderingFilter
from rest_framework.viewsets import ModelViewSet
from django_filters import FilterSet, AllValuesFilter, DateTimeFilter, NumberFilter
from rest_framework.generics import ListAPIView
from rest_framework.parsers import MultiPartParser, FormParser

# Create your views here.

class LostItemView(generics.ListCreateAPIView):
    queryset = LostItem.objects.all()
    serializer_class = LostItemSerializer


class LostObjectView(generics.ListCreateAPIView):
    queryset = LostObject.objects.all()
    serializer_class = LostObjectSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        date = serializer.validated_data['date']
        city = serializer.validated_data['city']
        user_id = serializer.validated_data['user_id']
        #is_matched = serializer.validated_data['is_matched']
        #obj = Point(lat, long)
        #serializer.validated_data['geometry'] = geom
        serializer.save()
        return Response(serializer.data)

class LostObjectFilter(ListAPIView):
    queryset = LostObject.objects.all()
    serializer_class = LostObjectSerializer
    filter_backends = [DjangoFilterBackend]
    filterset_fields = ['date', 'city', 'is_matched', 'user_id']


class LostObjectDetailsView(generics.RetrieveUpdateDestroyAPIView):
    queryset = LostObject.objects.all()
    serializer_class = LostObjectSerializer





class LostItemFilter(ListAPIView):
    queryset = LostItem.objects.all()
    serializer_class = LostItemSerializer
    filter_backends = [DjangoFilterBackend]
    filterset_fields = ['id', 'type', 'serial_number', 'brand', 'color']




class FoundObjectFilter(ListAPIView):
    queryset = FoundObject.objects.all()
    serializer_class = FoundObjectSerializer
    filter_backends = [DjangoFilterBackend]
    filterset_fields = ['longitude', 'latitude', 'city', 'user_id', 'is_matched']




class FoundItemFilter(ListAPIView):
    queryset = FoundItem.objects.all()
    serializer_class = FoundItemSerializer
    filter_backends = [DjangoFilterBackend]
    filterset_fields = ['id', 'type', 'color', 'brand', 'serial_number']


class LostItemDetailsView(generics.RetrieveUpdateDestroyAPIView):
    queryset = LostItem.objects.all()
    serializer_class = LostItemSerializer


class LostPersonView(generics.ListCreateAPIView):
    http_method_names = ['post']
    queryset = LostPerson.objects.all()
    serializer_class = LostPersonSerializer


class Lost_PersonView2(generics.UpdateAPIView):
    queryset = LostPerson.objects.all()
    serializer_class = Lost_PersonSerializer
    lookup_field = 'id'


class Lost_PersonView(generics.RetrieveUpdateDestroyAPIView):
    http_method_names = ['get']
    queryset = LostPerson.objects.all()
    serializer_class = Lost_PersonSerializer
    lookup_field = 'id'


class LostPersonDetailsView(generics.RetrieveUpdateDestroyAPIView):
    queryset = LostPerson.objects.all()
    serializer_class = LostPersonSerializer


class LostPersonImageView(generics.ListCreateAPIView):
    queryset = LostPersonImage.objects.all()
    serializer_class = LostPersonImageSerializer

class LostPersonImageDetailsView(generics.RetrieveUpdateDestroyAPIView):
    queryset = LostPersonImage.objects.all()
    serializer_class = LostPersonImageSerializer


class FoundObjectView(generics.ListCreateAPIView):
    queryset = FoundObject.objects.all()
    serializer_class = FoundObjectSerializer

class FoundObjectDetalisView(generics.RetrieveUpdateDestroyAPIView):
    queryset = FoundObject.objects.all()
    serializer_class = FoundObjectSerializer


class FoundItemView(generics.ListCreateAPIView):
    queryset = FoundItem.objects.all()
    serializer_class = FoundItemSerializer

class FoundItemDetailsView(generics.RetrieveUpdateDestroyAPIView):
    queryset = FoundItem.objects.all()
    serializer_class = FoundItemSerializer


class FoundPersonView(generics.ListCreateAPIView):
    http_method_names = ['post']
    queryset = FoundPerson.objects.all()
    serializer_class = FoundPersonSerializer


class Found_PersonView(generics.RetrieveUpdateDestroyAPIView):
    http_method_names = ['get']
    queryset = FoundPerson.objects.all()
    serializer_class = Found_PersonSerializer
    lookup_field = 'id'


class FoundPersonImageView(generics.ListCreateAPIView):
    queryset = FoundPersonImage.objects.all()
    serializer_class = FoundPersonImageSerializer


class MapView(generics.ListAPIView):
    queryset = FoundItem.objects.select_related('id')
    serializer_class = MapSerializer
@api_view(['GET'])
def comp_lostView(request, city):
    lost_obj = LostObject.objects.filter(city=city)
    serializer = LostObjectSerializer(lost_obj, many=True)
    return Response(serializer.data)

#class IntroductionViewSet(ModelViewSet):
    #queryset = LostObject.objects.all()
    #serializer_class = LostObjectSerializer
    #filter_backends = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    #filterset_fields = ['id', 'date', 'city', 'is_matched', 'user_id']
    #search_fields = ['city']
    #ordering_fields = ['date', 'id']
    #ordering = ['id']
class Comp_ViewSet(viewsets.ViewSet):

    def retrieve(self, request, pk):
        try:
            lost_obj = LostObject.objects.get(pk=pk)
        except LostObject.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = LostObjectSerializer(lost_obj)
        return Response(serializer.data)