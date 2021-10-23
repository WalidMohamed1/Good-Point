from rest_framework import generics
from rest_framework import status
from rest_framework.generics import GenericAPIView
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from .serializers import SignupSerializer, LogoutSerializer, WhoFoundItemSerializer, IdCardSerializer
from .models import User
from find_losts.models import LostObject, FoundObject

# Create your views here.


class SignupView(generics.CreateAPIView):
    queryset = User.objects.all()
    serializer_class = SignupSerializer


class LoginView(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        user = request.user
        user_pic = ""
        id_card_pic = ""
        losts = list(LostObject.objects.filter(user_id=user.pk).values('id'))
        founds = list(FoundObject.objects.filter(user_id=user.pk).values('id'))
        if user.profile_pic:
            user_pic = user.profile_pic.url
        if user.id_card_pic:
            id_card_pic = user.id_card_pic.url
        response = {
            'id': user.pk,
            'username': user.first_name,
            'email': user.username,
            'phone': user.phone,
            'city': user.city,
            'birthdate': user.birthdate,
            'profile_pic': user_pic,
            'id_card_pic': id_card_pic,
            'losts': losts,
            'founds': founds
        }
        return Response({'user': response}, status=status.HTTP_200_OK)


class LogoutView(GenericAPIView):
    serializer_class = LogoutSerializer
    permission_classes = {IsAuthenticated, }

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_204_NO_CONTENT)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class WhoFoundItemView(generics.RetrieveAPIView):
    queryset = User.objects.all()
    serializer_class = WhoFoundItemSerializer
    lookup_field = 'id'


class SetIdCard(generics.UpdateAPIView):
    queryset = User.objects.all()
    serializer_class = IdCardSerializer
    permission_classes = [IsAuthenticated]

    def patch(self, request, *args, **kwargs):
        print(request.data)
        user = request.user
        serializer = self.serializer_class(user, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status.HTTP_202_ACCEPTED)

        return Response(serializer.errors, status.HTTP_406_NOT_ACCEPTABLE)
