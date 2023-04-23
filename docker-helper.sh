#!/usr/bin/env sh

USAGE="Usage: $0 {dev|testing|usage}"
STARTDIR=$(dirname "$0")

dev() {
	cd "$STARTDIR/docker-compose/dev" || err_msg "Couldn't find dev folder??"
	printf "Starting dev containers...\n"
	printf "(Remember, you can live edit using this environment,\n"
	printf "There SHOULD be no need to restart the containers!)\n\n"

	trap "cleanup_containers" INT
	docker-compose up -d --build
	docker-compose logs -f
}

testing() {
	cd "$STARTDIR/docker-compose/testing" || err_msg "Couldn't find testing folder??"
	printf "Starting test containers...\n\n"

	trap "cleanup_containers" INT
	docker-compose up --abort-on-container-exit --build
	cleanup_containers
}

cleanup_containers() {
	echo # Slightly tidier?
	printf "Stopping and removing containers...\n\n"
	docker-compose down
}

main() {
	case "$1" in
		dev|testing|usage)
			("$1");;
		*)
			err_msg "$USAGE"
	esac
}

err_msg() {
	printf "%s\n" "$1" >&2
	exit 1
}

usage() {
	printf "%s\n" "$USAGE"
}

main "$@"

