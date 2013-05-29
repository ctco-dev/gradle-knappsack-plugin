Gradle Knappsack Plugin
=======================

This plug-in provides means to connect to a Knappsack server to perform artifact deployment. It handles authentication and supports SSL connections as well. All operations are performed using plain HTTP requests.

Example build.gradle:
---------------------

```groovy
apply plugin: 'knappsack'

knappsack {
    url = 'https://knappsack.sample.com'
    userName = 'DeployBot'
    password = 'qwerty' // Only for presentation purposes! :-)
    applicationId = 1
    groupId = 2
    storageId = 3
    version = '1.2.3'
    whatsNew = 'Recent changes in version 1.2.3'
    artifactFileName = 'build/application.ipa'

    keyStoreFileName = 'cacerts'
    keyStorePassword = 'azerty'
}
```

```bash
gradle knappsackUpload
```

Example command line usage:
---------------------------
```bash
gradle knappsackUpload -Pknappsack.url='https://knappsack.sample.com' -Pknappsack.user.name='DeployBot' -Pknappsack.password='qwerty' -Pknappsack.app.id=1 -Pknappsack.group.id=2 -Pknappsack.storage.id=3 -Pknappsack.version='1.2.3' -Pknappsack.whatsNew='Recent changes in version 1.2.3' -Pknappsack.artifact='build/application.ipa' -Pknappsack.keystore.file='cacerts' -Pknappsack.keystore.password='azerty'
```

knappsack extension parameters:
-------------------------------

### **url**
required: yes  
property: _knappsack.url_  
Knappsack server URL. Both HTTP and HTTPS connections are supported.  

### **userName**
required: yes  
property: _knappsack.user.name_  
User credential for authentication with Knappsack.  

### **password**
required: yes  
property: _knappsack.user.password_  
User password for authentication with Knappsack.  

### **applicationId**
required: yes  
property: _knappsack.app.id_  
Application id the artifact belongs to.  

### **groupId**
required: yes  
property: _knappsack.group.id_  
Group id the artifact should be available to.  

### **storageId**
required: yes  
property: _knappsack.storage.id_  
Storage id where the artifact should be kept.  

### **version**
required: yes  
property: _knappsack.version_  
Artifact version.  

### **whatsNew**
required: yes when _whatsNewFileName_ is not defined  
property: _knappsack.whatsnew_  
The message desribing recent changes in the application version. This parameter can not be used with whatsNewFileName.

### **whatsNewFileName**
required: yes when _whatsNew_ is not defined  
property: _knappsack.whatsnew.file_  
A file name containing the message decribing recent changed in the application version. This parameter can not be used with whatsNew.

### **artifactFileName**
required: yes  
property: _knappsack.artifact_  
A file name of the artifact file to upload.  

### **keyStoreFileName**
required: no  
property: _knappsack.keystore.file_  
The file name of a key store to use for HTTPS connections to the server. When the keyStoreFileName parameter is defined the given key store is used instead of the default one. This feature can be used when the Knappsack server uses a self-signed ceritficate and you don't want to modify the default Java key store.

### **keyStorePassword**
required: yes if _keyStoreFileName_ is defined  
property: _knappsack.keystore.password_  
A password for the key store file.

