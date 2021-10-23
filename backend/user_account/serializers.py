from rest_framework import serializers
from rest_framework_simplejwt.tokens import RefreshToken, TokenError
from .models import User


class SignupSerializer(serializers.ModelSerializer):
    username = serializers.CharField(max_length=150)
    phone = serializers.CharField(max_length=20, min_length=5)
    class Meta:
        model = User
        fields = ['username', 'password', 'first_name', 'phone', 'city', 'birthdate', 'profile_pic']

    def validate(self, attrs):
        exist_email = User.objects.filter(username=attrs.get('username', '')).exists()
        exist_phone = User.objects.filter(phone=attrs.get('phone', '')).exists()
        if exist_email and exist_phone:
            raise serializers.ValidationError(
                {
                    'error': {
                        'username': 'Email already exists',
                        'phone': 'Phone number already exists'
                    }
                })
        if exist_email:
            raise serializers.ValidationError({'error': {'username': 'Email already exists', 'phone': ''}})
        if exist_phone:
            raise serializers.ValidationError({'error': {'username': '', 'phone': 'Phone number already exists'}})
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


class WhoFoundItemSerializer(serializers.ModelSerializer):
    name = serializers.CharField(source='first_name')
    email = serializers.CharField(source='username')

    class Meta:
        model = User
        fields = ['name', 'email', 'phone']


class IdCardSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = ['id_card_pic']
