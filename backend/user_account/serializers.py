from rest_framework import serializers
from rest_framework_simplejwt.tokens import RefreshToken, TokenError
from .models import User


class SignupSerializer(serializers.ModelSerializer):
    username = serializers.CharField(max_length=150)
    email = serializers.EmailField(max_length=65, min_length=4)
    password = serializers.CharField(max_length=65, min_length=8, write_only=True)
    first_name = serializers.CharField(max_length=255, min_length=2)
    phone = serializers.CharField(max_length=20, min_length=5)
    city = serializers.CharField(max_length=35)
    birthdate = serializers.DateField
    profile_pic = serializers.ImageField

    class Meta:
        model = User
        fields = ['email', 'password', 'username', 'first_name', 'phone', 'city', 'birthdate', 'profile_pic']

    def validate(self, attrs):
        if User.objects.filter(email=attrs.get('email', '')).exists():
            raise serializers.ValidationError({'email': {'Email already exists'}})
        if User.objects.filter(phone=attrs.get('phone', '')).exists():
            raise serializers.ValidationError({'phone': {'Phone number already exists'}})
        return super().validate(attrs)

    def create(self, validated_data):
        return User.objects.create_user(**validated_data)


class LogoutSerializer(serializers.Serializer):
    token = serializers.CharField()

    def validate(self, attrs):
        self.token = attrs['refresh']
        return attrs

    def save(self, **kwargs):
        try:
            RefreshToken(self.token).blacklist()
        except TokenError:
            self.fail('bad_token')

    def update(self, instance, validated_data):
        pass

    def create(self, validated_data):
        pass
