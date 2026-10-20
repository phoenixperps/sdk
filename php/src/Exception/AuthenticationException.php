<?php

declare(strict_types=1);

namespace PhoenixPerps\Exception;

/** 401: the key is missing, wrong, revoked, expired or of the other environment. */
final class AuthenticationException extends PhoenixPerpsException
{
}
