from rest_framework import generics
from rest_framework import status
from rest_framework.generics import GenericAPIView
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from .serializers import SignupSerializer, LogoutSerializer

# Create your views here.


class SignupView(generics.CreateAPIView):
    serializer_class = SignupSerializer


class LoginView(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        user = request.user
        response = {
            'username': user.first_name,
            'email': user.email,
            'phone': user.phone,
            'city': user.city,
            'birthdate': user.birthdate,
            'profile_pic': user.profile_pic.url
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
