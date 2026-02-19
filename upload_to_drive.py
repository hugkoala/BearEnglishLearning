#!/usr/bin/env python3
"""Upload APK to Google Drive using device-code-like OAuth flow."""

import os
import sys
import json
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.http import MediaFileUpload
from google.oauth2.credentials import Credentials

SCOPES = ['https://www.googleapis.com/auth/drive.file']
TOKEN_FILE = os.path.expanduser('~/.config/gdrive_token.json')

# Use rclone's built-in client ID (public, safe to use)
CLIENT_CONFIG = {
    "installed": {
        "client_id": "202264815644.apps.googleusercontent.com",
        "client_secret": "X4Z3ca8xfWDb1Voo-F9a7ZxJ",
        "auth_uri": "https://accounts.google.com/o/oauth2/auth",
        "token_uri": "https://oauth2.googleapis.com/token",
        "redirect_uris": ["urn:ietf:wg:oauth:2.0:oob", "http://localhost"]
    }
}

def get_credentials():
    creds = None
    if os.path.exists(TOKEN_FILE):
        creds = Credentials.from_authorized_user_file(TOKEN_FILE, SCOPES)

    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            from google.auth.transport.requests import Request
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_config(CLIENT_CONFIG, SCOPES)
            # Use console-based OOB flow for headless environments
            creds = flow.run_local_server(port=0, open_browser=False)

        os.makedirs(os.path.dirname(TOKEN_FILE), exist_ok=True)
        with open(TOKEN_FILE, 'w') as f:
            f.write(creds.to_json())

    return creds

def find_or_create_folder(service, folder_name):
    """Find or create a folder in Google Drive."""
    query = f"name='{folder_name}' and mimeType='application/vnd.google-apps.folder' and trashed=false"
    results = service.files().list(q=query, spaces='drive', fields='files(id, name)').execute()
    files = results.get('files', [])

    if files:
        return files[0]['id']

    folder_metadata = {
        'name': folder_name,
        'mimeType': 'application/vnd.google-apps.folder'
    }
    folder = service.files().create(body=folder_metadata, fields='id').execute()
    print(f"Created folder: {folder_name}")
    return folder.get('id')

def upload_file(file_path, folder_name='BearEnglishLearning'):
    """Upload a file to Google Drive."""
    creds = get_credentials()
    service = build('drive', 'v3', credentials=creds)

    folder_id = find_or_create_folder(service, folder_name)

    file_name = os.path.basename(file_path)
    file_metadata = {
        'name': file_name,
        'parents': [folder_id]
    }
    media = MediaFileUpload(file_path, mimetype='application/vnd.android.package-archive', resumable=True)

    print(f"Uploading {file_name} ({os.path.getsize(file_path) / 1024 / 1024:.1f} MB)...")
    file = service.files().create(body=file_metadata, media_body=media, fields='id, webViewLink').execute()

    print(f"\n✅ Upload successful!")
    print(f"   File ID: {file.get('id')}")
    print(f"   Link: {file.get('webViewLink')}")
    return file

if __name__ == '__main__':
    apk_path = sys.argv[1] if len(sys.argv) > 1 else 'app/build/outputs/apk/debug/app-debug.apk'
    if not os.path.exists(apk_path):
        print(f"❌ File not found: {apk_path}")
        sys.exit(1)
    upload_file(apk_path)
