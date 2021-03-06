# Generated by Django 3.1.4 on 2021-02-16 21:54

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Candidate',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('percent', models.DecimalField(decimal_places=4, max_digits=5)),
                ('is_matched', models.BooleanField(default=False)),
            ],
            options={
                'db_table': 'candidate',
            },
        ),
        migrations.CreateModel(
            name='FoundObject',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateTimeField()),
                ('longitude', models.DecimalField(decimal_places=10, default=0.0, max_digits=14)),
                ('latitude', models.DecimalField(decimal_places=10, default=0.0, max_digits=14)),
                ('city', models.CharField(max_length=35)),
                ('is_matched', models.BooleanField(default=False)),
            ],
            options={
                'db_table': 'found_object',
            },
        ),
        migrations.CreateModel(
            name='LostObject',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateTimeField()),
                ('city', models.CharField(max_length=35)),
                ('is_matched', models.BooleanField(default=False)),
            ],
            options={
                'db_table': 'lost_object',
            },
        ),
        migrations.CreateModel(
            name='FoundItem',
            fields=[
                ('id', models.OneToOneField(db_column='id', on_delete=django.db.models.deletion.CASCADE, primary_key=True, serialize=False, to='find_losts.foundobject')),
                ('type', models.CharField(max_length=20)),
                ('color', models.CharField(max_length=20)),
                ('brand', models.CharField(max_length=50)),
                ('description', models.CharField(max_length=700)),
                ('serial_number', models.CharField(blank=True, max_length=100, null=True)),
                ('image', models.ImageField(unique=True, upload_to='')),
            ],
            options={
                'db_table': 'found_item',
            },
        ),
        migrations.CreateModel(
            name='FoundPerson',
            fields=[
                ('id', models.OneToOneField(db_column='id', on_delete=django.db.models.deletion.CASCADE, primary_key=True, serialize=False, to='find_losts.foundobject')),
                ('name', models.CharField(blank=True, max_length=150, null=True)),
            ],
            options={
                'db_table': 'found_person',
            },
        ),
        migrations.CreateModel(
            name='LostItem',
            fields=[
                ('id', models.OneToOneField(db_column='id', on_delete=django.db.models.deletion.CASCADE, primary_key=True, serialize=False, to='find_losts.lostobject')),
                ('type', models.CharField(max_length=20)),
                ('color', models.CharField(max_length=20)),
                ('brand', models.CharField(max_length=50)),
                ('description', models.CharField(max_length=700)),
                ('serial_number', models.CharField(blank=True, max_length=100, null=True)),
                ('image', models.ImageField(blank=True, null=True, unique=True, upload_to='')),
            ],
            options={
                'db_table': 'lost_item',
            },
        ),
        migrations.CreateModel(
            name='LostPerson',
            fields=[
                ('id', models.OneToOneField(db_column='id', on_delete=django.db.models.deletion.CASCADE, primary_key=True, serialize=False, to='find_losts.lostobject')),
                ('name', models.CharField(max_length=150)),
            ],
            options={
                'db_table': 'lost_person',
            },
        ),
        migrations.CreateModel(
            name='MatchedPerson',
            fields=[
                ('id_fp', models.OneToOneField(db_column='found_person_id', on_delete=django.db.models.deletion.CASCADE, primary_key=True, related_name='match', serialize=False, to='find_losts.foundobject')),
                ('date_of_receiving', models.DateTimeField(auto_now_add=True)),
                ('percent', models.DecimalField(decimal_places=4, max_digits=5)),
            ],
            options={
                'db_table': 'matched_Person',
            },
        ),
    ]
