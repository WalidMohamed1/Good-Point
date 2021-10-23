from django.contrib import admin
from .models import LostObject, LostItem


# Register your models here.
@admin.register(LostObject)
class LostObjectAdmin(admin.ModelAdmin):
    list_display = ['id', 'date', 'city', 'user_id']


@admin.register(LostItem)
class LostItemAdmin(admin.ModelAdmin):
    list_display = ['id', 'type', 'color', 'brand', 'description', 'serial_number', 'image']
