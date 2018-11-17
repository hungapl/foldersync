#### Step 1 Find the path where mtp mounted the device storage and create a symbolic link
- ``` > cd /var/run/user/$UID/gvfs/mtp/ ```
> UID is the ID of the current Linux user

- Use *ls* to list all the mounted devices, mounted directory name should starts with *mtp:host*
- Use *ln* to create a symbolic link to a directory with simplified path, e.g. ln -s /var/run/user/$UID/gvfs/mtp\:host\=XXXX ~/mtp_mounted

#### Step 2 Update config file to reference the photos directory
- Copy the *foldersync.config.template* to a local directory and rename it to *foldersync.config*
- Replace the sourceDirectory and destinationDirectory with the correct paths


#### Step 3 Starts synchronization
- Import this project in IntelliJ
- The application requires a single named argument:
``` -config $path_to_foldersync_config_file [-verbose] ```
