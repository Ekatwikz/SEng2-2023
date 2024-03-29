# SEng2 Project
## Aircraft Booking System
NB: For development and/or testing, you'll want [WSL2](https://learn.microsoft.com/en-us/windows/wsl/install) to use my `./docker-helper.sh` script if you're on windows

## Development
- [Install Docker](https://docs.docker.com/get-docker/)  
(Enable WSL2 during the installation prompts, if Windows)
- [Open a terminal in the current folder](https://www.google.com/search?q=how+to+open+a+terminal+in+current+folder) (WSL2 if Windows)
- Ctrl+C, Ctrl+V, Enter:
```
clear && ./docker-helper.sh dev
```

- Open http://localhost:3069 in your browser

- Press Ctrl+C when you're done, my helper script takes care of cleaning up the containers

NB: the database's volume is persistent  
You can nuke it using [`docker volume rm [...]`](https://docs.docker.com/engine/reference/commandline/volume_rm/#description)

## Testing
### Backend
- Intall docker, open terminal, etcetc
- Ctrl+C, Ctrl+V, Enter:
```
clear && ./docker-helper.sh testing
```

Backend tests' source are in ./backend/src/test/java/pw/react/backend

### Frontend
TODO!

## 'Connection denied' or 'Connection refused' error when starting the script:
- [Add your user to docker group](https://stackoverflow.com/a/65240108) and try again:
```
sudo gpasswd -a $USER docker # Add yourself to the docker group
newgrp docker # Log in to the group
clear && ./docker-helper.sh [...] # Retry
```

