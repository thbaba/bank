LOG_FOLDER="push-logs"


createImage() {
  local folderName=$1
  local imageName=$2
  local logFile="$LOG_FOLDER/$imageName.log"
  echo "WAITING $imageName"
  if (docker build "$folderName" \
    -f "$folderName/Dockerfile" \
    -t "thedeno/$imageName" \
    -t "thedeno/$imageName:0.0.1" && \
  docker image push "thedeno/$imageName" --all-tags) > "$logFile" 2>&1; then
    echo "SUCCESS $imageName"
  else
    echo "FAIL $imageName"
    cat "$logFile"
  fi
}

mkdir -p "$LOG_FOLDER"

createImage accounts accounts &
createImage cards cards &
createImage configserver configserver &
createImage discoveryserver discoveryserver &
createImage gatewayserver gatewayserver &

wait

rm -rf "$LOG_FOLDER"