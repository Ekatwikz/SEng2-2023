# SEng2 Project
## Aircraft Booking System
NB: For development and/or testing, you'll want [WSL2](https://learn.microsoft.com/en-us/windows/wsl/install) to use my `./docker-helper.sh` script if you're on windows

## Development
- [Install Docker](https://docs.docker.com/get-docker/)  
(Enable WSL2 during the installation prompts, if Windows)
- [Open a terminal in the current folder](https://www.google.com/search?q=how+to+open+a+terminal+in+current+folder) (WSL2 if Windows)
- Ctrl+C, Ctrl+V, Enter:
```
clear && ./docker-helper dev
```
or (if above does not work)

```
clear && ./docker-helper.sh dev
```

### IF you get 'Connection denied' or 'Connection refused' error
- [Add your user to docker group and try again](https://stackoverflow.com/a/65240108)

- Open http://localhost:3069 in your browser

### IF you get 'Error: EACCES: permission denied, unlink ...'
- [Try following](https://github.com/vercel/next.js/issues/8908) it helped me:

```
sudo chown -R $USER <project-dir>
```

- Press Ctrl+C when you're done, my helper script takes care of cleaning up the containers

NB: the database's volume is persistent  
You can nuke it using [`docker volume rm [...]`](https://docs.docker.com/engine/reference/commandline/volume_rm/#description)

## Testing
### Backend
- Intall docker, open terminal, etcetc
- Copy-Paste and Run:
```
clear && ./docker-helper testing
```

Backend tests' source are in ./backend/src/test/java/pw/react/backend

### Frontend
TODO!

