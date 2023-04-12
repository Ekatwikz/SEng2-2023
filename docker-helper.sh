#!/usr/bin/env sh

USAGE="Usage: $0 {dev|testing|usage}"
STARTDIR=$(dirname "$0")

err_msg() {
	printf "%s\n" "$1" >&2
	exit 1
}

usage() {
	printf "%s\n" "$USAGE"
}

dev() {
	cd "$STARTDIR/docker-compose/dev" || err_msg "Couldn't find dev folder??"
	printf "Starting dev containers...\n"
	printf "(Remember, you can live edit using this environment,\n"
	printf "There SHOULD be no need to restart the containers!)\n\n"

	trap "printf '\nStopping and removing containers...\n\n' && docker-compose down" INT
	docker-compose up -d --build
	docker-compose logs -f
}

testing() {
	cd "$STARTDIR/docker-compose/testing" || err_msg "Couldn't find testing folder??"
	printf "Starting test containers...\n\n"

	trap "printf '\nStopping and removing containers...\n\n' && docker-compose down" INT
	docker-compose up --abort-on-container-exit --build
	docker compose down
}

main() {
	case "$1" in
		dev|testing|usage)
			("$1");;
		*)
			err_msg "$USAGE"
	esac
}

main "$@"

